FROM ubuntu:22.04

ENV TZ=Europe/Warsaw


RUN apt-get update && apt-get install -y wget vim git curl nano

RUN apt-get install -y build-essential unzip

RUN apt-get install -y fp-compiler fp-units-fcl fp-utils

RUN useradd -ms /bin/bash student 
RUN adduser student sudo

EXPOSE 80

USER student
WORKDIR /home/student/
RUN mkdir projobj
WORKDIR /home/student/projobj

COPY src /home/student/projobj

CMD /home/student/projobj/skrypt.sh