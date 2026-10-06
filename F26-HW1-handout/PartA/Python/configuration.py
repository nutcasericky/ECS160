class Configuration:
    __instance = None
    def __new__(cls):
        if cls.__instance is None:
            cls.__instance = super().__new__(cls)
        return cls.__instance
    
    def __init__(self):
        if not hasattr(self, "__initialized"):
            self.appName = "ECS160-HW1"
            self.logLevel = "INFO"
            self.maxConnections = 32
            self.debugMode = True
            self.__initialized = True

    @classmethod
    def get_instance(cls):
        return cls()