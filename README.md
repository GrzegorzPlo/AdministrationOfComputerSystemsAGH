# Using Kibana and Grafana for monitoring of Virtual Machines and micro-services
by Michał Sterzel & Grzegorz Płóciennik

## [Grafana](https://grafana.com/) - the open observability platform
### Opis ogólny
Jedną z dwóch części projektu realizowanego w ramach przedmiotu **Administracja Systemów Komputerowych** była konfiguracja środowiska oraz implementacja dashboardów na platformie Grafana dla wybranej aplikacji oraz maszyny wirtualnej.

### Przygotowanie oraz konfiguracja środowiska

#### Aplikacja oraz Prometheus
Jako źródło źródło danych dla Grafany posłóżyła nam aplikacja zaimplementowana przez nas w trakcie poprzedniego semestru w ramach przedmiotu Wprowadzenie do Aplikacji Internetowych.
System załączony do niniejszego repozytorium składa się z trzech serwisów:
- Movie Recommendation
- Movie Base
- User

Budowa systemu została oparta o [Spring boot](https://spring.io/projects/spring-boot) oraz [Maven](https://maven.apache.org/) co pozwoliło na skorzystanie m.in. z następujących zależności:
- [Spring Boot Starter Actuator](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator)
- [Micrometer Registry Prometheus](https://mvnrepository.com/artifact/io.micrometer/micrometer-registry-prometheus)

Umożliwiły one zbieranie logów, które możliwe są do przeformatowania przez Grafanę.

Do obsługi usługi Prometheus niezbędne okazało się być zainstalowanie narzędzi dostępnych pod [tym adresem](https://prometheus.io/download/).

Następnie dostosowano konfigurację usługi w następujący sposób (treść całego pliku dostępna w repozytorium pod nazwą prometheus.yml) :
```
scrape_configs:
  - job_name: 'MovieRecommendationService'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 3s
    static_configs:
      - targets: ['localhost:8081', 'localhost:8082', 'localhost:8084']
        labels:
          application: 'Movie Recommendation Service'
```
Po czym uruchomiono serwis na domyślym porcie - http://localhost:9090/

Z racji ograniczeń sprzętowych, w naszym przypadku, aplikacja, serwis Prometheus oraz Grafana zostały zainstalowane lokalnie, jednak [dokumentacja Prometheus](https://prometheus.io/docs/prometheus/latest/installation/) oraz [dokumentacja Grafana](https://grafana.com/docs/grafana/latest/setup-grafana/installation/docker/) oferują także komplet instrukcji do uruchomienia usług za pomocą Dockera.

#### Maszyna wirtualna
Do utworzenia maszyny wirtualnej z systemem [Ubuntu Desktop](https://ubuntu.com/download/desktop) zdecydowano się użyć darmowego narzędzia [Oracle VirtualBox](https://www.virtualbox.org/).
Wykonano standardową instalację zgodną z [poniższą instrukcją](https://ubuntu.com/tutorials/how-to-run-ubuntu-desktop-on-a-virtual-machine-using-virtualbox#1-overview).

Aby umożliwić pracę z lokalną maszyną, zmieniono ustawienie _Settings -> Network -> Attached to_ z NAT na Bridged Adapter.

Następnie na maszynie lokalnej przy pomocy kontenera Docker zainstalowano komplet narzędzi potrzebnych do uruchomienia serwisu [Zabbix](https://www.zabbix.com/) z bazą danych Postgres zgodnie z Przykładem nr 2 dostępnym pod [poniższym adresem](https://www.zabbix.com/documentation/current/en/manual/installation/containers).

Aby umożliwić zbieranie danych o maszynie wirtualnej, zainstalowano na niej Zabbix Agent2 zgodnie z [poniższą instrukcją](https://medium.com/geekculture/how-to-install-zabbix-agent2-on-linux-c603023207d2).
Uzupełniając w pliku konfiguracyjnym Zabbix Agent2 informacje:
- Server
- ServerActive
- Hostname
- ListenPort

Server oraz ServerActive to adres IPv4 serwera Zabbix, a Hostname to nazwa maszyny wirtualnej - można ją sprawdzić za pomocą komendy:
```
hostname
```
ListenPort ustawiono na 10050.
Dodatkowo, przed uruchomieniem Zabbix Agent2, skonfigurowano także firewall wykonując po kolei komendy:
- Sprawdzenie statusu firewall
```
sudo ufw status
```
- Uruchomienie firewall (jeśli poprzednia komenda zwróci wartość inactive)
```
sudo ufw enable
```
- Zezwolenie na ruch po porcie 10050 dla TCP
```
sudo ufw allow 10050/tcp
```


Następnym krokiem była weryfikacja adresu IPv4 maszyny wirtualnej za pomocą komendy:
```
ifconfig
```
Dosponując kompletem informacji na temat Zabbix Agent2, możliwe było dodanie nowego hosta do Zabbix Server (zgodnie z ww. instrukcją).


#### Grafana
Konfiguracja usługi Grafana rozpoczęła się od instalacji narzędzi dostępnych [pod tym adresem](https://grafana.com/grafana/download?platform=windows).

Po uruchomieniu usługi (domyślnie pod adresem http://localhost:3000/) przechodząc w menu do _Home -> Administration -> Plugins_ wyszukano [dodatek Prometheus](https://grafana.com/grafana/plugins/prometheus/), zainstalowano go i zezwolono na użycie.
Wówczas możliwe było skonfigurowanie Data Source dla Grafana zgodnie z [poniższą instrukcją](https://prometheus.io/docs/visualization/grafana/).
Uzupełniono Nazwę (Name) oraz adres URL usługi - pozostałe ustawienia zachowano jako domyślne.

W analigiczny sposób dodano [plugin Zabbix](https://grafana.com/grafana/plugins/alexanderzobnin-zabbix-app/), skonfigurowano Data Source podając URL w postaci HOST_SERVER_IP/api_jsonrpc.php oraz dane logowania do Zabbix API.

### Przygotowanie dashboardów Grafana
