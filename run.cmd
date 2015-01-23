@echo off

@set M2_REPO=C:\Users\aleksandr.stserbakov\.m2\repository
@set VERTX_HOME=C:\Tools\vert.x-2.1.5

@if '%VERTX_ENV%' equ 'true' goto run

@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\hazelcast-3.2.3.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\jackson-annotations-2.2.2.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\jackson-core-2.2.2.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\jackson-databind-2.2.2.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\netty-all-4.0.21.Final.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\vertx-core-2.1.5.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\vertx-hazelcast-2.1.5.jar
@set CLASSPATH=%CLASSPATH%;%VERTX_HOME%\lib\vertx-platform-2.1.5.jar

@set CLASSPATH=%CLASSPATH%;%M2_REPO%/javax/activation/activation/1.1/activation-1.1.jar
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/javax/mail/mail/1.4.7/mail-1.4.7.jar
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/commons-validator\commons-validator\1.4.1\commons-validator-1.4.1.jar
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/commons-beanutils\commons-beanutils\1.8.3\commons-beanutils-1.8.3.jar;
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/commons-digester\commons-digester\1.8.1\commons-digester-1.8.1.jar;
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/commons-logging\commons-logging\1.2\commons-logging-1.2.jar;
@set CLASSPATH=%CLASSPATH%;%M2_REPO%/commons-collections\commons-collections\3.2.1\commons-collections-3.2.1.jar

@set PATH=%PATH%;%VERTX_HOME%\bin

@set VERTX_ENV=true

:run

vertx run core.Server -cp target\vertx-0.0.1-SNAPSHOT.jar
