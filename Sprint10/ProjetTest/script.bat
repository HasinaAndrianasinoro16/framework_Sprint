cd "D:\L2\apache-tomcat-8\webapps\fmrwk\Sprint10\MyFrameWork"
javac -cp .\WEB-INF\lib\*.jar -d .\WEB-INF\classes .\src\*.java

cd "D:\L2\apache-tomcat-8\webapps\fmrwk\Sprint10\MyFrameWork\WEB-INF\classes"
jar cvfm etu1762.jar META-INF/MANIFEST.MF ./

copy etu1762.jar "D:\L2\apache-tomcat-8\webapps\fmrwk\Sprint10\ProjetTest\WEB-INF\lib"

cd "D:\L2\apache-tomcat-8\webapps\fmrwk\Sprint10\ProjetTest"
javac -parameters -cp ".\WEB-INF\lib\*;.\WEB-INF\lib" -d .\WEB-INF\classes src\*.java

cd "D:\L2\apache-tomcat-8\webapps\fmrwk\Sprint10\ProjetTest\WEB-INF\lib"
jar -cvf ProjetdeTest.war .\

copy ProjetdeTest.war "D:\L2\apache-tomcat-8\webapps"



