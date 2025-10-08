import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Customer } from '../../types/Customer';
import { of } from 'rxjs';
import { CustomerTS } from '../../types/tstypes/Customerts';

@Component({
  selector: 'app-customersample',
  standalone: true,
  imports: [],
  templateUrl: './customersample.component.html',
  styleUrls: ['./customersample.component.css']
})
export class CustomersampleComponent {
  customer: CustomerTS;
  constructor() {
    this.customer = new CustomerTS("Vivek", "ex@ex.com", "basu", "12345", "USER", "1");
  }
}

