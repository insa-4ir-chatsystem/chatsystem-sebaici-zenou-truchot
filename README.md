# Project : chat system

Autors: Kelian Sebaici, Théophile Zenou-Truchot

This project aims to deliver a peer-to-peer app for communication on a local network. It provides an easy way to communicate within a company. It must be easy to use and easy to install.
The app runs on the version 8 of the Java Development Kit and uses Maven :

If Maven is not installed on your machine, use the following commands to install it (Linux only) :

```bash
mkdir -p ~/bin
cd ~/bin
wget https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.tar.gz -O maven.tar.gz
tar xf maven.tar.gz
echo 'export PATH=~/bin/apache-maven-3.9.5/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

With the current version of the Chat-System you will be able to :

- Use a graphical interface.
- Create a profile by setting a pseudo.
- Change your pseudo and inform other people on the network of this change.
- Update the user list in the process when someone changes his nickname, when you press the "Refresh" button and once a minute automatically. (Only the refresh button causes the view to be actualised)

First you will need to extract the downloaded zip/tar.gz file and open a terminal where you extracted it.

Use the following command lines on your machine to launch the app:

```bash
mvn clean package
```

```bash
java -jar target/ChatSytem-1.0-SNAPSHOT.jar
```

The app is able to run on Linux and on Windows and no compatibility problem has been found yet, if so, please notify us by sending an email to <sebaici@insa-toulouse.fr> and <zenou-trucho@insa-toulouse.fr>.
