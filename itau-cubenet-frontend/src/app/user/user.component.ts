import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { User } from '../models/user';
import { FormsModule } from '@angular/forms';
import { UserService } from '../service/user.service';
import { Chart } from 'chart.js/auto';

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

  public chart: any;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getUsers();
    this.createChart();
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
          this.createChart();
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
        this.createChart();
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
        this.createChart();
      },
      error: (err) => {
        console.error('Error updating user:', err);
      }
    });
  }


  createChart() {
    const userPercentages = this.users.map(user => user.participation); // calculate percentages
    const userLabels = this.users.map(user => user.firstName); // get user names for labels

    this.chart = new Chart("MyChart", {
      type: 'doughnut',
      data: {
        labels: userLabels,
        datasets: [{
          label: 'User Percentages',
          data: userPercentages,
          backgroundColor: this.generateColors(userPercentages.length), // generate colors
          hoverOffset: 4
        }],
      },
      options: {
        aspectRatio: 2.5
      }
    });
  }

  generateColors(count: number): string[] {
    const colors = [];
    for (let i = 0; i < count; i++) {
      colors.push(this.getRandomColor()); // generate a random color for each user
    }
    return colors;
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

}
