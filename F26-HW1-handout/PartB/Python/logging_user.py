import json
from functools import wraps


def log(method):
    @wraps(method)
    def wrapper(self, *args, **kwargs):
        arguments = [json.dumps(value) for value in args]
        arguments.extend(
            f"{name}={json.dumps(value)}" for name, value in kwargs.items()
        )
        print(
            f"[LOG] {type(self).__name__}.{wrapper.__name__}"
            f"({', '.join(arguments)})"
        )
        return method(self, *args, **kwargs)

    return wrapper
