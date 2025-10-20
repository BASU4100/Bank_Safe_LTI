import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { HttpClient } from "@angular/common/http";
import { Transaction } from "../types/Transaction";
import { Observable } from "rxjs";
import { Customer } from "../types/Customer";
import { Account } from "../types/Account";

@Injectable({
  providedIn: "root",
})
export class BankService {
  private baseUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  addCustomer(customer: Customer): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/customers`, customer);
  }

  editCustomer(customer: Customer): Observable<any> {
    console.log(customer);
    const url = `${this.baseUrl}/customers/${customer.customerId}`;
    return this.http.put<any>(url, customer);
  }

  deleteCustomer(customerId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/customers/${customerId}`);
  }

  getCustomerById(customerId: number): Observable<Customer> {
    return this.http.get<Customer>(
      `${this.baseUrl}/customers/${customerId}`
    );
  }

  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(
      `${this.baseUrl}/customers`
    );
  }

  addAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(`${this.baseUrl}/accounts`, account);
  }

  editAccount(account: Account): Observable<Account> {
    console.log(account);
    const url = `${this.baseUrl}/accounts/${account.accountId}`;
    return this.http.put<Account>(url, account);
  }

  deleteAccount(accountId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/accounts/${accountId}`);
  }

  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(
      `${this.baseUrl}/accounts`
    )
  }

  getAccountsByUser(userId: string | null): Observable<Account[]> {
    return this.http.get<Account[]>(
      `${this.baseUrl}/accounts/user/${userId}`
    )
  }

  getAccountById(accountId: number): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/accounts/${accountId}`
    )
  }

  performTransaction(transaction: Transaction): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/transactions`, transaction);
  }

  getAllTranactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(
      `${this.baseUrl}/transactions`
    );
  }

  getAllTransactionsByCustomerId(customerId: string): Observable<any> {
    return this.http.get<any>(
      `${this.baseUrl}/transactions/user/${customerId}`
    );
  }

}