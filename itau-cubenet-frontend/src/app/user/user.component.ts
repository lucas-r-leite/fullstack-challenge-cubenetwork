import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { User } from '../models/user';
import { FormsModule } from '@angular/forms';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [NgFor, FormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {

  users: User[] = [];
  user: User = {} as User;
  selectedUser: User = {} as User;
  isUpdateModalOpen = false;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.userService.getUsers().subscribe(users => this.users = users);
  }

  onSubmit(): void {
    this.userService.addUser(this.user)
      .subscribe({
        next: (user) => {
          console.log('User added:', user);
          this.getUsers();
          this.user = {} as User;  // Clear the form fields
        },
        error: (err) => {
          console.error('Error adding user:', err);
        }
      });
  }

  deleteUser(id: number): void {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        console.log('User deleted successfully!');
        this.getUsers(); // Refresh the user list
      },
      error: (err) => {
        console.error('Error deleting user:', err);
      }
    });
  }


  openUpdateModal(user: User): void {
    this.selectedUser = { ...user };
    this.isUpdateModalOpen = true;
  }

  closeUpdateModal(): void {
    this.isUpdateModalOpen = false;
  }

  onUpdate(): void {
    this.userService.updateUser(this.selectedUser).subscribe({
      next: (user) => {
        console.log('User updated:', user);
        this.getUsers();
        this.closeUpdateModal();
      },
      error: (err) => {
        console.error('Error updating user:', err);
      }
    });
  }

}
