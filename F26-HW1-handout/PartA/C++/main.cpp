#include <cassert>
#include <iostream>

#include "Configuration.h"

int main() {
    Configuration& config1 = Configuration::getInstance();
    Configuration& config2 = Configuration::getInstance();

    std::cout << "config1 address: " << &config1 << std::endl;
    std::cout << "config2 address: " << &config2 << std::endl;
    assert(&config1 == &config2);

    std::cout << "All assertions passed" << std::endl;
    return 0;
}
