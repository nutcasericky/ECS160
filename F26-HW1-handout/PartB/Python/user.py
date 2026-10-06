from logging_user import log

class User():
    def get_name(self):
        pass

    def get_email(self):
        pass

    def set_email(self, email):
        pass


class AdminUser(User):
    def __init__(self, name, email):
        self._name = name
        self._email = email

    @log
    def get_name(self):
        return self._name

    @log
    def get_email(self):
        return self._email

    @log
    def set_email(self, email):
        self._email = email
