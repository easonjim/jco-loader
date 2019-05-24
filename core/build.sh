# change version snapshot or release
# deploy snapshot, pom version must be include -SNAPSHOT keywork
mvn clean deploy
# deploy release, pim version must be exclude -SNAPSHOT keywor
mvn clean deploy -P release