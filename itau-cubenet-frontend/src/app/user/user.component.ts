import { Component, OnInit, OnDestroy } from '@angular/core';
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

export class UserComponent implements OnInit, OnDestroy {

  users: User[] = [];
  user: User = {} as User;
  selectedUser: User = {} as User;
  isUpdateModalOpen = false;

  public charts: { enterprise: string, chart: Chart<"doughnut", number[], string> }[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.getUsers();
  }

  ngOnDestroy(): void {
    this.destroyCharts();
  }

  getUsers(): void {
    this.userService.getUsers().subscribe(users => {
      this.users = users;
      this.createCharts();
    });
  }

  onSubmit(): void {
    this.userService.addUser(this.user).subscribe({
      next: (user) => {
        console.log('User added:', user);
        this.getUsers();
        this.user = {} as User;
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
        this.getUsers();
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

  createCharts(): void {
    this.destroyCharts();
    const enterprises = this.groupUsersByEnterprise(this.users);

    enterprises.forEach((users, enterprise) => {
      const chartData = this.getChartData(users);
      const chart = new Chart(`chart-${enterprise}`, {
        type: 'doughnut',
        data: chartData,
        options: {
          aspectRatio: 2.5
        }
      });
      this.charts.push({ enterprise, chart });
    });
  }

  destroyCharts(): void {
    this.charts.forEach(chart => chart.chart.destroy());
    this.charts = [];
  }

  groupUsersByEnterprise(users: User[]): Map<string, User[]> {
    return users.reduce((map, user) => {
      if (!map.has(user.enterprise)) {
        map.set(user.enterprise, []);
      }
      map.get(user.enterprise)?.push(user);
      return map;
    }, new Map<string, User[]>());
  }

  getChartData(users: User[]): { labels: string[], datasets: { label: string, data: number[], backgroundColor: string[], hoverOffset: number }[] } {
    const userPercentages = users.map(user => user.participation);
    const userLabels = users.map(user => user.firstName);

    return {
      labels: userLabels,
      datasets: [{
        label: 'User Percentages',
        data: userPercentages,
        backgroundColor: this.generateColors(userPercentages.length),
        hoverOffset: 4
      }],
    };
  }

  generateColors(count: number): string[] {
    const colors = [];
    for (let i = 0; i < count; i++) {
      colors.push(this.getRandomColor());
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
