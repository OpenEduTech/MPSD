cmake_minimum_required(VERSION 3.10 FATAL_ERROR)

# glog
set(GLOG_INCLUDE_DIRS "${CMAKE_SOURCE_DIR}/thirdparty/glog/include" CACHE PATH "Path to glog include directory")
set(GLOG_LIBRARIES "${CMAKE_SOURCE_DIR}/thirdparty/glog/lib/libglog.so" CACHE FILEPATH "Path to glog libraris") 
set(GLOG_LIBRARY_DIRS "${CMAKE_SOURCE_DIR}/thirdparty/glog/lib" CACHE PATH "Path to glog library directory")