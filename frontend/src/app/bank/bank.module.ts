import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { BankRoutingModule } from "./bank-routing.module";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { RouterModule } from "@angular/router";
import { AccountComponent } from "./components/account/account.component";
import { EditAccountComponent } from "./components/accountedit/accountedit.component";
import { CustomersComponent } from "./components/customer/customer.component";
import { EditCustomerComponent } from "./components/customeredit/customeredit.component";
import { DashboardComponent } from "./components/dashboard/dashboard.component";
import { TransactionComponent } from "./components/transaction/transaction.component";
import { NavBarComponent } from "../navbar/navbar.component";

@NgModule({
  declarations: [
    AccountComponent,
    EditAccountComponent,
    CustomersComponent,
    EditCustomerComponent,
    DashboardComponent,
    TransactionComponent,
    NavBarComponent
  ],
  imports: [
    CommonModule,
    BankRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule
  ],
  exports: [
    
  ]
})
export class BankModule {}
