# Use OpenJDK for running a Scala/Java application
FROM openjdk:11.0.13

# Set working directory
WORKDIR /app

# Install SBT (Scala Build Tool)
RUN apt-get update && \
    apt-get install -y curl && \
    curl -L -o sbt.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-1.4.9.deb && \
    dpkg -i sbt.deb && \
    rm sbt.deb

# Copy project files
COPY . .

# Build the project
RUN sbt clean compile dist

# Expose necessary port
EXPOSE 9000

# Command to run the application
CMD ["sbt", "run"]
