from configuration import Configuration


def main():
    config1 = Configuration.get_instance()
    config2 = Configuration.get_instance()

    print(f"config1 address: {hex(id(config1))}")
    print(f"config2 address: {hex(id(config2))}")
    assert config1 is config2

    print("All assertions passed")


if __name__ == "__main__":
    main()
