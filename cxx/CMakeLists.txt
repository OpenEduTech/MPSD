cmake_minimum_required(VERSION 3.10 FATAL_ERROR)

project(demo LANGUAGES CXX)

add_subdirectory(thirdparty)



add_executable(${PROJECT_NAME} main.cpp)

target_include_directories(${PROJECT_NAME}
  PRIVATE
    ${PROJECT_SOURCE_DIR}/include 
    ${GLOG_INCLUDE_DIRS}
    )

target_link_libraries(${PROJECT_NAME}
  PRIVATE
    ${GLOG_LIBRARIES}
  )
message(${CMAKE_SOURCE_DIR})
target_compile_definitions(${PROJECT_NAME} 
  PRIVATE
    GOOGLE_LOG_DIR="${CMAKE_SOURCE_DIR}/log"
    GOOGLE_LOG_LEVEL="google::INFO"
    GOOGLE_LOG_FILE_NAME="my_log.log"
  )
