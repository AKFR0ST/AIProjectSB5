FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openjdk-21-jdk nginx && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY target/AIProjectSB5-0.0.1-SNAPSHOT.jar app.jar
COPY mobile.html /usr/share/nginx/html/mobile.html
COPY nginx.conf /etc/nginx/sites-available/default
COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 80 8088
CMD ["/start.sh"]