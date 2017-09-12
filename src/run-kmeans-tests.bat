echo -----------------------------------------------------------------------
echo Start.
echo -----------------------------------------------------------------------
set cp=-classpath ..\..\..\lib\*;. Main
set wp=workspacepath=C:\Users\Piotr\GitHubProjects\clustering
set dt=data=\data\
set dtc=data=[CUSTOM]\data\checkins-pyramid\
set al=algorithm
set kmeans=%al%=kMeans
set pikmea=%al%=pikMeans
set pr=parameters=dump

set f=Checkins

java %cp% %wp% %dt%%f%.txt %kmeans% %pr%;k:5;maxIterations:10
java %cp% %wp% %dtc%       %pikmea% %pr%;k:5;maxIterations:10;deepest:15;depth:15;starting:3;plot
