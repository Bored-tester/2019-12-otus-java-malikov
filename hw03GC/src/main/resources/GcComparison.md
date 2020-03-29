# GC comparison

## Test class
LeakingClass does the following:
 1. Generates 40000 objects called Bullets
 2. Forgets half of them after 0.5 sec
 3. Stores what is left (20000) in some array, which cause "memory leak"
 4. Repeat    

Heap size is 1Gb.
Run parameters (other than GC):
```java
 -Xms1024m -Xmx1024m -verbose:gc -XX:+PrintGCDetails -XX:-PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump -XX:+DisableExplicitGC -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCDateStamps -Xloggc:gclog.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=2000k
``` 
I did two runs each with CMS and G1 garbage collectors.  
Examples of GC logs attached in gcLogs folder.

## CMS data
Both runs took around 4 minutes and 13-14 seconds.
![](images/ScreenshotCMS1.png)
![](images/ScreenshotCMS2.png)
Both runs were able to generate a total of 18440000 Bullets (+x, where x < 40000).
It means that 9220000 Bullets were stored and <40000 more were generated at the time of out of memory.

Total time the application was stopped for by all GC: 23.99 seconds.  
Total time the application was stopped for by CMS GC: 4.91 seconds.  
Total time the application was stopped for by Full GC: 17.11 seconds.  
Total time the application was stopped for by VM overhead operations: 1.97865 seconds.

Throughput = 90.52%.  
Number of full GC pauses = 11.  
Number of GC pauses = 69.  
Avg gc pause = 0.07109.  
Min/max gc pause = 0.00468/0.16058s.  

## G1 data
Both runs took around 4 minutes and 52-58 seconds.
![](images/ScreenshotG1_1.png)
![](images/ScreenshotG1_2.png)
Both runs were able to generate a total of 19480000 Bullets (+x, where x < 40000).
It means that 9740000 Bullets were stored and <40000 more were generated at the time of out of memory.

Total time the application was stopped for by all GC: 59 seconds.  
Total time the application was stopped for by CMS GC: 3.26 seconds.  
Total time the application was stopped for by Full GC: 48.55 seconds.  
Total time the application was stopped for by VM overhead operations: 7.18411 seconds.  

Throughput = 80.17%.  
Number of full GC pauses = 31.  
Number of GC pauses = 119.  
Avg gc pause = 0.02742.  
Min/max gc pause = 0.00068/0.21177s.  

## Comparison

1. In terms of CPU load there is no considerable difference. 
In both cases GC rarely consumed more than 10% and these spikes started to appear only when we were 
getting close to the limit. However CMS spend around 50 seconds on concurrent (agains 7.2 seconds for G1) work without 
stopping the application which means more CPU usage.

2. In terms of Heap usage we can see that G1 GC lasted considerably longer and managed to store 
520000 Bullets more (around 5%) before failing (probably due to lowering young generation space). But it worked slower 
as 5% growth in objects count came at a cost of 16% increase of all time execution which was mostly to a bigger number 
of full gc pauses.  
Not sure if it is a pro or a con. Pro - if you don't care much about reliability, con - otherwise.  
Anyway G1 seems to be better in terms of surviving extreme loads but worse in case of leaks, as it agonizes longer.

3. On average G1 pauses are much shorter (more than two times), which means better response times, but there are a 
lot more of them. If response time is critical, G1 would be more suitable.  
Although there is an issue with much longer time spent on overhead VM operations in G1 case and average pauses there 
are bigger than for CMS. But these might be cause only by the last agonising seconds of work.

4. G1 is less predictable. Memory usage graph is more chaotic. CMS is more consistent but more rigid in terms of 
memory allocation strategies.

