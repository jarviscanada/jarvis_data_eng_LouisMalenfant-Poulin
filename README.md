# Linux Cluster Monitoring Agent
## Introduction
This tool is a tool that allows a user to monitor different values about their machine in real time. This tool collects data about the machine such as the model and the clock speed and stores it inside a psql databse for future referencing. 

## Architecture and Design
![](1assets/Design.png)

We can see in the diagram that all the information is being stored in the psql databse that is inside the Docker container

## Database and Tables

host_info : 
```
id = The primary key that is automatically set by the database
hostname = The name of the host
cpu_number = The identifying number of the CPU
cpu_architecture = The name of the architecture type of the CPU
cpu_model = The model of the CPU
cpu_mhz = The clock speed of the CPU
l2_cache = The size of the L2 cache, which is the slowest but the largest cache of the CPU
timestamp = The moment at which the data was taken
total_mem = The total memory of the CPU
```
host_usage :
```
timestamp = The moment at which the data was taken
host_id = The number that defines the host
memory_free = The amount of unallocated memory
cpu_idle = The percentage of the CPU that s currently unused
cpu_kernel = The percentage of the CPU that is assigned to the kernel
disk_io = The amount of reads and writes in process
disk_available = The amount of MB available on the disk
```

## Usage
### Starting up the application
To first start the application, we must first create the docker instance and create the psql database with the 2 tables host_info and host_usage
```
#The command to create/start/stop the docker instance, only needs username and password if creating 
./scripts/psql_docker.sh create/start/stop username password

#The command to initialize the database with the right tables
psql -h host -U user -d agent -f /sql/dd.sql
```

### Using host_info.sh
This script simply collects static data about the cpu and stores it inside the host_info table inside the created database
```
./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```

### Using host_usage.sh
This script collects memory usage informationand stores it inside the host_usage table inside the created database
```
./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```

### Crontab
You can automate host_usage.sh through a cronjob
```
#First you need to bet in the crontab
crontab -e
#Then you need to add the job
* * * * * $PATH/host_usage.sh psql_host psql_port db_name psql_user psql_password > templog.log
#Finally you can verify through the log file that everything is working properly
car templog.log
```

## Improvements

1. The program could collect alert us when certain data points are past their normal values, such as if the memory usage is too high or too low.
2. The program could also make some aggregated data points to inform us on the averages for data usage so that we can better see the general memory usage.
3. The program could use a front end infrastructure to better show with visuals the values instead of only with numbers.


