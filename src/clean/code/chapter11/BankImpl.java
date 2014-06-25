package clean.code.chapter11;

// Bank.java (suppressing package names...)

import clean.code.added.to.make.code.build.Account;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// The abstraction of a bank.
interface Bank {
  Collection<Account> getAccounts();

  void setAccounts(Collection<Account> accounts);
}

// BankImpl.java

// The "Plain Old Java Object" (POJO) implementing the abstraction.
public class BankImpl implements Bank {
  private List<Account> accounts;

  public Collection<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(Collection<Account> accounts) {
    this.accounts = new ArrayList<Account>();
    for (Account account : accounts) {
      this.accounts.add(account);
    }
  }
}

// BankProxyHandler.java

// "InvocationHandler" required by the proxy API.
class BankProxyHandler implements InvocationHandler {
  private Bank bank;

  public BankProxyHandler(Bank bank) {
    this.bank = bank;
  }

  // Method defined in InvocationHandler
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    String methodName = method.getName();
    if (methodName.equals("getAccounts")) {
      bank.setAccounts(getAccountsFromDatabase());
      return bank.getAccounts();
    } else if (methodName.equals("setAccounts")) {
      bank.setAccounts((Collection<Account>) args[0]);
      setAccountsToDatabase(bank.getAccounts());
      return null;
    } else {
      // ...
    }
    // Dummy to get it to compile
    return null;
  }

  // Lots of details here:
  protected Collection<Account> getAccountsFromDatabase() {
    // ...
    // Dummy to get it to compile
    return null;
  }

  protected void setAccountsToDatabase(Collection<Account> accounts) {
    // ...
  }

  // Somewhere else...
  public static void main(String[] args) {
    Bank bank = (Bank) Proxy.newProxyInstance(
        Bank.class.getClassLoader(),
        new Class[]{Bank.class},
        new BankProxyHandler(new BankImpl())
    );
  }
}