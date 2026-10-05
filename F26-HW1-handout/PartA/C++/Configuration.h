#ifndef CONFIGURATION_H
#define CONFIGURATION_H

#include <string>

class Configuration {
public:

private:

    std::string appName;
    std::string logLevel;
    int maxConnections;
    bool debugMode;
};

#endif
