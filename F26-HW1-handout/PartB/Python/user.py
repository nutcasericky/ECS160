

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

    def get_name(self):
        return self._name

    def get_email(self):
        return self._email

    def set_email(self, email):
        self._email = email
