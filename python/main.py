from loguru import logger
import os
import sys

if __name__ == '__main__':
    logger.add(f"{__file__[:__file__.rfind('/')]}/demo.log")
    logger.info("Hello World from CI test")
