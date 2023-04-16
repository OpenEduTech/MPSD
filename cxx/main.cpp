#include <iostream>
#include "glog/logging.h"
int main(int argc,char **argv){
    google::SetLogDestination(google::GLOG_INFO, argv[0]);
    google::InitGoogleLogging(argv[0]);
    LOG(INFO)<<"Hello world from CI test";
    LOG(ERROR)<<"Hello world from CI test";
    return 0;
}