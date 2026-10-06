from user import AdminUser
from logging_user import LoggingUser

def main():
    user = LoggingUser(AdminUser("Alice", "alice@example.com"))
    user.set_email("alice@ucdavis.edu")
    user.get_name()
    user.get_email()
    
if __name__ == "__main__":
    main()
