### Project Description
rastered.io - Server-side image processing with remote realtime viewport.
### Run using Docker Compose
1. Copy the following content into a file named "docker-compose.yml".
2. Make sure docker and docker-compose are installed on your Linux machine.
3. CD into the folder which contains the docker-compose file, then run "docker-compose -p rio up -d".
4. Open a browser try the project by accessing http://127.0.0.1:3000
```
version: '3'

services:
  rio:
    image: pascalmatthaeus/io.rastered:latest
    ports:
      - "8080:8080"
      - "3000:3000"
    environment:
      - TZ=Europe/London
    restart: unless-stopped

  ome:
    image: pascalmatthaeus/io.rastered.stream:latest
    ports:
      - "3333:3333"
      - "3478:3478"
      - "10006-10010:10006-10010/udp"
      - "1935:1935"
    environment:
        - TZ=Europe/London
    restart: unless-stopped
```
