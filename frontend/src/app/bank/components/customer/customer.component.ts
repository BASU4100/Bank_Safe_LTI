import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankService } from '../../services/bank.service';
import { Customer } from '../../types/Customer';

@Component({
  selector: 'app-customers',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
})
export class CustomersComponent implements OnInit {
  isFormSubmitted = false;
  customerSuccess: string = '';
  customerError: string = '';
  customerForm!: FormGroup;
  customer: Customer | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private banksService: BankService
  ) { }

  ngOnInit(): void {
    this.customerForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, this.noSpecialCharacters]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      role: ['', Validators.required]
    });
  }

  private noSpecialCharacters(control: any): {[key: string]: boolean} | null {
    if (control.value && /[\W_]/.test(control.value)) {
        return { specialCharacters: true };
      }
      return null;
}

  onSubmit(): void {
    if (this.customerForm.valid) {
      this.banksService.addCustomer(this.customerForm.value).subscribe({
        next: (response) => {
          this.customer = response;
          this.customerSuccess = 'Customer created successfully';
          this.customerError = '';
          this.customerForm.reset();
        },
        error: (error) => this.customerError = error.error
      });
    } else {
      this.customerError = 'Please fill out all required fields correctly.';
      this.customerSuccess = '';
    }
  }
}