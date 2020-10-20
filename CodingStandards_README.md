# PaintSplat_Group13

Coding Standards for Components: It is recommended to write components name by its purpose. This approach improves the readability and maintainability of code.

Coding Standards for Classes: Usually class name should be noun starting with uppercase letter. If it contains multiple word than every inner word should start with uppercase.
Eg: String, StringBuffer, Dog

Coding Standards for Interface: Usually interface name should be adjective starting with uppercase letter. If it contains multiple word than every inner word should start with uppercase.
Eg: Runnable, Serializable, Comparable

Coding Standards for Methods: Usually method name should either be verb or verb noun combination starting with lower letter. If it contains multiple word than every inner word should start with uppercase.
Eg: print(), sleep(), setSalary()

Coding Standards for Variables: Usually variable name should be noun starting with lowercase letter. If it contains multiple word than every inner word should start with uppercase.
Eg: name, age. mobileNumber

Coding Standards for Constants: Usually constant name should be noun. It should contain only uppercase If it contains multiple word than words are separated with ( _ ) underscore symbol. Usually we declare constants with public static and final modifiers.

Java Bean Coding Standards: A Java Bean is a simple java class with private properties and public getter and setter methods
  Getter Methods:
    It should be public method. Method name should be prefixed with “get”. It should not take any argument
  Setter Methods:
    It should be public method. Return Type should be void. Method name should be prefixed with “set”. It should take some argument
  public class StudentBean{
    private String name;
    public void setName(String name){
      this.name=name;
    }
    public String getName(){
      return name;
    }
  }

Coding convention for Listners:
  To register a Listner method name should prefixed with add
  Eg: public void addMyAccountListner( MyActionListner);
  To unregister a Listner method name should prefixed with remove
  Eg: public void removeMyAccountListner( MyActionListner);
  
  
  Git
      Branches should be named using camelCase.
        Example: testBranch
        
      Git Flow:
        (1) Go to master branch (git checkout master)
        (2) Pull the latest changes (git pull)
        (3) Create new branch (git checkout -b newBranchName)
        (4) Make your changes
        (5) Add your changes to your branch (git add .)
        (6) Commit your changes (git commit -m "meaningful message")
        (7) push your changes to your branch (git push)
        If you have finished a feature
            Repeat steps (1) & (2)
            (8) Post into the teams chat let people know you are about to perform a pull request
            (9) Go back to your own branch (git checkout branchName)
            (10) Rebase off the master branch (git rebase -i master)
            (11) Go to git hub
            (12) Send a pull request (should be at the top of the screen)
            (13) Post a message to the teams letting people know you sent the pull request
            (14) When the pull request has been fully completed post again to let everyone know.
        else
            (8) Get back to work :)
     
     If you finish a "coding" session push your changes to your branch (steps 5, 6 & 7)
     

This content was taken from https://android.jlelse.eu/java-coding-standards-ee1687a82ec2 for reference if you need more clarification on any of the points.
