# Pull base image.
FROM java:8u45-jre

# Define working directory.
WORKDIR /data

ADD target/eventapp-*-SNAPSHOT.war /data/

EXPOSE 8080

CMD java -server -XX:+DoEscapeAnalysis -XX:+UseStringDeduplication -XX:+UseCompressedOops -XX:+UseG1GC \
		-XX:MaxGCPauseMillis=5 -XX:MaxTenuringThreshold=5 -Xmx1024M -Xms1024M -XX:+AggressiveOpts \
		-Dsun.net.inetaddr.ttl=1200 -Dsun.net.inetaddr.negative.ttl=10 \
		-jar eventapp-*-SNAPSHOT.war --spring.profiles.active=prod
