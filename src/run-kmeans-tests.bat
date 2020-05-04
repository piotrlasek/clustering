echo -----------------------------------------------------------------------
echo Start.
echo -----------------------------------------------------------------------
set cp=-classpath ..\..\..\lib\*;. Main
set wp=workspacepath=C:\Users\piotr\IdeaProjects\clustering\
set dt=data=\data
set dtc=data=\data\random_100000.csv
set al=algorithm
set kmeans=%al%=k-Means
set pikmea=%al%=pi-Means
set pr=parameters=dump

java %cp% %wp% %dtc%       %kmeans% %pr%;k:5;maxIterations:10
java %cp% %wp% %dtc%       %pikmea% %pr%;k:8;maxIterations:5;deepest:15;depth:15;starting:1;plot
