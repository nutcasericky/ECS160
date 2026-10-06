There is a disadvantage: 
putting `@log` on the methods requires editing the class definition and applies
logging to every instance of that class. An object wrapper can instead wrap
selected instances at runtime, wrap any implementation of `User`, and add or
compose behavior without editing the original class.

