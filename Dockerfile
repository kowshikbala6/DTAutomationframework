FROM maven:3.8.1-openjdk-11

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src
COPY testng.xml .

# Install dependencies
RUN mvn clean install -DskipTests -q

# Run tests
CMD ["mvn", "test", "-Dmaven.test.failure.ignore=true"]

# Optional: For running specific test suite
# ENTRYPOINT ["mvn", "test", "-Dtest=${TEST_CLASS:=*}"]
