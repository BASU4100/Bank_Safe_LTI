import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map, Observable } from "rxjs";
// import { User } from "../types/user";
import { Customer } from "src/app/bank/types/Customer";
import { environment } from "src/environments/environment";
import { Token } from "@angular/compiler";
 
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loginUrl: string = environment.apiUrl;
  httpOptions: {headers: HttpHeaders} = {
    headers: new HttpHeaders({
      'Content-Type' : 'application/json',
      'Access-Control-Allow-Origin': '*'
    })
  };
 
  constructor(private http: HttpClient) {}
 
  login(user: Partial<Customer>): Observable<{ [key: string]: string }> {
    return this.http.post<{token: string}>(`${this.loginUrl}/customer/login`, user, this.httpOptions);
  }
 
  getToken() {
    return localStorage.getItem('token');
  }
 
  getRole() {
    return localStorage.getItem('role');
  }
 
  // getUsers(): Observable<Customer[]> {
  //   return ;
  // }
 
  createUser(user: Customer): Observable<Customer> {
    return this.http.post<Customer>(`${this.loginUrl}/customer/register`, user);
  }
}