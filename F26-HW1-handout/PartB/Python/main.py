from user import AdminUser


def main():
    user = AdminUser("Alice", "alice@example.com")
    user.set_email("alice@ucdavis.edu")
    user.get_name()
    user.get_email()



if __name__ == "__main__":
    main()
