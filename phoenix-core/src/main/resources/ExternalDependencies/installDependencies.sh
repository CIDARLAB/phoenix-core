mvn install:install-file -Dfile=RavenCAD-1.1-SNAPSHOT.jar -DgroupId=Raven -DartifactId=RavenCAD -Dversion=1.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=Datasheet_Generator-1.0-SNAPSHOT.jar -DgroupId=org.owl -DartifactId=Datasheet_Generator -Dversion=1.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=clotho3api-jar-with-dependencies9.jar -DgroupId=org.clothocad -DartifactId=Clotho3JavaAPI -Dversion=3.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=miniEugene-core-1.0.0-jar-with-dependencies.jar -DgroupId=org.cidarlab -DartifactId=miniEugene -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=jsbml-1.0-with-dependencies.jar -DgroupId=org.sbml -DartifactId=jsbml -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=COPASIWS_Services.jar -DgroupId=org.copasi -DartifactId=copasiws -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=copasi.jar -DgroupId=org -DartifactId=copasi -Dversion=4.6 -Dpackaging=jar
mvn install:install-file -Dfile=libCopasiJava.so -DgroupId=org -DartifactId=libCopasiJava -Dversion=4.6 -Dpackaging=so -DgeneratePom=true -Dclassifier=sources
mvn install:install-file -Dfile=iBioSim.jar -DgroupId=org -DartifactId=ibiosim -Dversion=2.9 -Dpackaging=jar
