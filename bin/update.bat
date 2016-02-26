@echo off 
echo path:%~dp0
set base=%~dp0
set class_path=%base%
java  -classpath %class_path%  copyFilePack/FileOperateUtils
@pause