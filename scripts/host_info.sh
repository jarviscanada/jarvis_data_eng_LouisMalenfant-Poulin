#! /bin/bash

# save hostname as a variable
hostname=$(hostname -f)

# save the number of CPUs to a variable
lscpu_out=`lscpu`
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
# tip: `xargs` is a trick to remove leading and trailing white spaces
# tip: the $2 is instructing awk to find the second field

# hardware info
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture" | awk '{print $2}' | xargs )
cpu_model=$(echo "$lscpu_out" | egrep "Model:" | awk '{print $2}' | xargs )
cpu_mhz=$(echo "$lscpu_out" | egrep "Model name:" | awk '{print $7}' | xargs )
l2_cache=$(echo "$lscpu_out" | egrep "L2" | awk '{print $3,$4}' )
total_mem= $(vmstat --unit M | tail -1 | awk '{print $4}')
timestamp=date +"%Y-%m-%d %H:%M:%S" # current timestamp in `2019-11-26 14:40:19` format; use `date` cmd

# usage info
memory_free=$(vmstat --unit M | tail -1 | awk -v col="4" '{print $col}')
cpu_idle=$(vmstat -s | grep "idle" | awk '{print $1}')
cpu_kernel=$(cat /proc/meminfo | grep "Kernel" | awk '{print $2,$3}')
disk_io=$(vmstat --unit M -d | tail -1 | awk -v col="10" '{print $col}')
disk_available=$(df -BM | awk '{Total=Total+$4} END{print Total}')


insert_statement="INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, "timestamp", total_mem) VALUES(3, $3, $cpu_number, $cpu_architecture, $cpu_model, $cpu_mhz, $l2_cache, $timestamp, $total_mem);"

PGPASSWORD="rocky1234"

psql -h localhost -p $2 -U $4 -d $3 -c $insert_statement
