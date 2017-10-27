echo off
echo -----------------------------------------------------------------------
echo Start.
echo -----------------------------------------------------------------------
set cp=-classpath ..\..\..\lib\*;. Main
set wp=workspacepath=C:\Users\Piotr\GitHubProjects\clustering
set dt=data=\data\experiment
set al=algorithm
set _nbc=%al%=NBC
set cnbc=%al%=C-NBC
set _dbs=%al%=DBSCAN
set cdbs=%al%=C-DBSCAN
set pr=parameters=dump;plot;close_plot

rem set f=birch1
rem echo on
rem echo java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:50;
rem echo java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:4000;MinPts:15;
rem echo off
rem java -classpath ..\..\..\lib\*;. Main workspaceuath=C:\Users\Piotr\GitHubProjects\clustering data=\data\experiment\birch1.txt algorithm=NBC parameters=dump;plot;close_plot;k:75;

echo QUALITY TESTS
echo -----------------------------------------------------------------------
set f=birch3

java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:30;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:30;
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:50;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:50;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_nbc% %pr%;k:70;
java %cp% %wp% %dt%\%f%.txt %cnbc% %pr%;k:70;ic:%f%

java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:5000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:5000;MinPts:15;d:3;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:6000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:6000;MinPts:15;d:3;ic:%f%
java %cp% %wp% %dt%\%f%.txt %_dbs% %pr%;Eps:7000;MinPts:15;
java %cp% %wp% %dt%\%f%.txt %cdbs% %pr%;Eps:7000;MinPts:15;d:3;ic:%f%

echo -----------------------------------------------------------------------
echo Done.
echo -----------------------------------------------------------------------
