import json

from user import User


class LoggingUser(User):
    """Log User calls before delegating to the wrapped user."""

    def __init__(self, delegate):
        self._delegate = delegate

    def get_name(self):
        print(f"[LOG] {type(self._delegate).__name__}.get_name()")
        return self._delegate.get_name()

    def get_email(self):
        print(f"[LOG] {type(self._delegate).__name__}.get_email()")
        return self._delegate.get_email()

    def set_email(self, email):
        print(f"[LOG] {type(self._delegate).__name__}.set_email()")
        return self._delegate.set_email(email)