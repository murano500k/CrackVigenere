# CrackVigenere
Java application for cracking vigenere cipher.
## How to make and run:
git clone https://github.com/murano500k/CrackVigenere.git
cd CrackVigenere
./gradlew build
./gradlew run  -PappArgs="['PATH_TO_ENCRYPTED_TEXT_FILE']"
Gradle build command will also run tests. Results will be stored at:
./app/build/test-results/testDebugUnitTest/TEST-com.murano500k.crackvigenere.MainTest.xml
