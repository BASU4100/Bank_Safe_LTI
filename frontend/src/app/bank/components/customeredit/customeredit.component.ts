import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BankService } from '../../services/bank.service';
import { Customer } from '../../types/Customer';

@Component({
    selector: 'app-customeredit',
    templateUrl: './customeredit.component.html',
    styleUrls: ['./customeredit.component.scss'],
})
export class EditCustomerComponent implements OnInit {
    customerSuccess: string = '';
    customerError: string = '';
    customerForm!: FormGroup;
    customer: Customer;
    customerId: number;

    constructor(
        private formBuilder: FormBuilder,
        private banksService: BankService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.customerForm = this.formBuilder.group({
            name: ['', [Validators.required]],
            email: [{ value: '', disabled: true }, [Validators.required, Validators.email]],
            username: ['', [Validators.required, this.noSpecialCharacters]],
            password: ['', [Validators.required, Validators.minLength(8)]],
            role: ['', Validators.required]
        });
        this.route.params.subscribe(params => {
            this.customerId = params.customerId;
            this.loadCustomerDetails();
        });
    }

    loadCustomerDetails(): void {
        this.banksService.getCustomerById(this.customerId).subscribe({
            next: (customer) => {
                this.customer = customer;
                this.customerForm.patchValue({
                    name: customer.name,
                    email: customer.email,
                    username: customer.username,
                    password: customer.password,
                    role: customer.role
                });
            },
            error: () => {
                this.customerError = 'Error loading customer details.';
                console.log(this.customerError);
            }
        })
    }

    private noSpecialCharacters(control: any): {[key: string]: boolean} | null {
        if (control.value && /[\W_]/.test(control.value)) {
            return { specialCharacters: true };
          }
          return null;
    }

    onSubmit(): void {
        if (this.customerForm.valid) {
            const updatedCustomer: Customer = new Customer(this.customerForm.value);
            updatedCustomer.customerId = this.customerId+"";
            updatedCustomer.email = this.customer.email;
            this.banksService.editCustomer(updatedCustomer).subscribe({
                next: (response) => {
                    this.customer = response;
                    this.customerSuccess = 'Customer updated successfully';
                    this.customerError = '';
                    this.customerForm.reset();
                    this.router.navigate(['/bank']);
                }
            });
        } else {
            this.customerError = 'Please fill out all required fields correctly.';
            this.customerSuccess = '';
        }
    }
}