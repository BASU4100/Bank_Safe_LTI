import { Component } from '@angular/core';
import { AccountTS } from '../../types/tstypes/Accountts';

@Component({
  selector: 'app-accountsample',
  standalone: true,
  imports: [],
  templateUrl: './accountsample.component.html',
  styleUrls: ['./accountsample.component.css']
})
export class AccountsampleComponent {
  account: AccountTS /*= new AccountTS("C01", 500, "A01") */;
  constructor() {
    this.account = new AccountTS("1", 1000, "1");
  }
}
