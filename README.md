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
Dysponując kompletem informacji na temat Zabbix Agent2, możliwe było dodanie nowego hosta do Zabbix Server (zgodnie z ww. instrukcją).


#### Grafana
Konfiguracja usługi Grafana rozpoczęła się od instalacji narzędzi dostępnych [pod tym adresem](https://grafana.com/grafana/download?platform=windows).

Po uruchomieniu usługi (domyślnie pod adresem http://localhost:3000/) przechodząc w menu do _Home -> Administration -> Plugins_ wyszukano [dodatek Prometheus](https://grafana.com/grafana/plugins/prometheus/), zainstalowano go i zezwolono na użycie.
Wówczas możliwe było skonfigurowanie Data Source dla Grafana zgodnie z [poniższą instrukcją](https://prometheus.io/docs/visualization/grafana/).
Uzupełniono Nazwę (Name) oraz adres URL usługi - pozostałe ustawienia zachowano jako domyślne.

W analigiczny sposób dodano [plugin Zabbix](https://grafana.com/grafana/plugins/alexanderzobnin-zabbix-app/), skonfigurowano Data Source podając URL w postaci HOST_SERVER_IP/api_jsonrpc.php oraz dane logowania do Zabbix API.

### Przygotowanie dashboardów Grafana
Etap przygotowywania dashboardów i związanych z nimi obiektów opierał się o [oficjalną dokumentację producenta](https://grafana.com/docs/grafana/latest/) oraz poradniki dostępne w sieci.
Dla obu środowisk zdecydowano się przygotować dashboardy prezentujące przykładowe dane - nie skupiono się na ilości prezentowanych wizualizacji, ale na przedstawieniu najczęściej stosowanych dla dashboardów konfiguracji i ustawień. 


## [Kibana](https://www.elastic.co/kibana/) - a data visualization dashboard software for Elasticsearch
### Opis ogólny
Drugą częścią realizowanego przez nas projektu była konfiguracja środowiska oraz implementacja dashboardów na platformie Kibana dla wybranej aplikacji.

### Przygotowanie oraz konfiguracja środowiska

#### Aplikacja
Postanowiliśmy ponownie skorzystać z aplikacji użytej podczas konfiguracji usługi Grafana. Ze względu na sposób działania oprogramowania Kibana uznaliśmy, że dobrym rozwiązaniem będzie skomasowanie rozdzielonego frontendu oraz backendu aplikacji w całość, wspólnie istniejącą w obrębie jednego kontenera oraz łatwiejszą do jednoczesnego uruchomienia. W tym celu dwa pliki docker-compose.yml należące do front i backendu zostały połączone.

#### Kibana i Elasticsearch
Następnym krokiem było dodanie oprogramowania Kibana do istniejącej konfiguracji. W tym celu do pliku docker-compose.yml dodano poniższe wpisy:
```
  kibana:
    networks:
      - mysql-net
    image: docker.elastic.co/kibana/kibana:7.14.0
    ports:
      - 5601:5601
    depends_on:
      - elasticsearch
    environment:
      - ELASTICSEARCH_URL=http://elasticsearch:9200

  elasticsearch:
    networks:
      - mysql-net
    image: docker.elastic.co/elasticsearch/elasticsearch:7.14.0
    ports:
      - 9200:9200
    environment:
      - discovery.type=single-node
```

Uruchomienie kontenera przy pomocy komendy:
```
docker-compose up -d
```
pozwoliło nam uzyskać dostęp do strony głównej interfejsu graficznego usługi Kibana pod adresem http://localhost:5601/app/home#/:
![image](https://github.com/GrzegorzPlo/AdministrationOfComputerSystemsAGH/assets/103272466/62b754a2-7651-4bf1-b339-7cd2b21b5be2)

#### Logstash i pipeline z bazy MySQL
Mając dostęp do usługi Kibana potrzebowaliśmy połączyć ją z bazą MySQL wykorzysywaną przez aplikację. W tym celu skorzystaliśmy z oprogramowania Logstash wspieranego przez Elasticsearch właśnie w tym celu.
Dodanie oprogramowania przebiegło w podobny sposób jak w przypadku dodania Kibany i Elasticsearch:
```
  logstash:
    networks:
      - mysql-net
    image: docker.elastic.co/logstash/logstash:7.14.0
    volumes:
      - ./config/logstash.conf:/usr/share/logstash/pipeline/logstash.conf
      - ./config/mysql-connector-java-8.0.30.jar:/usr/share/logstash/mysql-connector-java-8.0.30.jar
    depends_on:
      - elasticsearch
      - mysqldb
```
Usługa Logstash potrzebowała jednak również plików konfigurujących na podstawie których miała pobierać dane z bazy MySQL jak również wtyczki mysql-jdbc. Wymagane pliki zostały umieszczone w katalogu config w katalogu głównym projektu. Ponieżej treść pliku logstash.conf:
```
input {
  jdbc {
    jdbc_connection_string => "jdbc:mysql://mysqldb:3306/filmRecommendationDb"
    jdbc_user => "root"
    jdbc_password => "root"
    jdbc_driver_library => "/usr/share/logstash/mysql-connector-java-8.0.30.jar"
    jdbc_driver_class => "com.mysql.jdbc.Driver"
    statement => "SELECT * FROM user WHERE id > :sql_last_value ORDER BY id ASC"
    schedule => "*/1 * * * *"
    use_column_value => true
    tracking_column => "id"
    tracking_column_type => "numeric"
    record_last_run => true
    clean_run => false
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "userdata"
  }
}
```
Z tego pliku Logstash odczytuje wszelkie parametry potrzebne do nawiązania połączenia z bazą MySQL oraz API Elasticsearch. 
Po uruchomieniu kontenera Logstash wykona zapytanie z pola statement na bazie danych, następnie utworzy index z nazwą wprowadzoną w pliku oraz udostępni te dane w interfejsie graficznym Kibany.
Dodatkowo wprowadzone do pliku zostało pole schedule z wartością */1* co powoduje, że Logstash w interwałach 1 minutowych będzie ponownie wykonywał zapytanie na bazie i uaktualniał dane w Kibanie.
W celu uniknięcia duplikacji danych flaga record_last_run została zaznaczona, a do pola statement została dodana odpowiednia reguła, która będzie zczytywać tylko wpisy o większej wartości pola id niż dotychaczasowe

#### Dołączenie indexu i utworzenie dashboard'u
Po dodaniu do konfiguracji usługi Logstash w Kibanie uzyskaliśmy dostęp do nowo utworzonego indexu userdata:
![image](https://github.com/GrzegorzPlo/AdministrationOfComputerSystemsAGH/assets/103272466/f5fa348a-e3a3-43c4-b545-4dbeaf0e6d2f)

Mając dostęp do indexu możemy zacząć tworzyć wizualizacje danych takie jak np. liczba obecnych użytkowników:
![image](https://github.com/GrzegorzPlo/AdministrationOfComputerSystemsAGH/assets/103272466/2ecc295e-adc5-4c9c-bc72-f4ec5f7c7a98)
