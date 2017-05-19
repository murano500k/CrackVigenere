# CrackVigenere
Java application for cracking vigenere cipher.
## How to make and run:
git clone https://github.com/murano500k/CrackVigenere.git 
<br/>
cd CrackVigenere
<br/>
./gradlew build
<br/>
./gradlew run  -PappArgs="['PATH_TO_ENCRYPTED_TEXT_FILE']"
<br/><br/>Example:<br/>
./gradlew run  -PappArgs="['/home/artem/projects/CrackVigenere/app/testencrypted.txt']"
<br/>
<br/>
<br/>
Gradle build command will also run tests. Results will be stored at:<br/>
./app/build/test-results/testDebugUnitTest/TEST-com.murano500k.crackvigenere.MainTest.xml
<br/>
<br/>
<br/>
app/src/main/java/com/murano500k/crackvigenere/DeVigenere.java - Decrypting logic<br/>
app/src/main/java/com/murano500k/crackvigenere/Main.java - Application Main class<br/>
app/src/test/java/com/murano500k/crackvigenere/MainTest.java - Tests<br/>

