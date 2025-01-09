import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-budget-exceeded-dialog',
  template: `
    <h2 mat-dialog-title>Warning</h2>
    <div mat-dialog-content>
      <p>{{message}}</p>
    </div>
    <div mat-dialog-actions>
      <button mat-button (click)="close()">OK</button>
    </div>
  `,
  styleUrls: ['./exceeded-dialog.component.css']
})
export class ExceededDialogComponent {
  constructor(private dialogRef: MatDialogRef<ExceededDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: { message: string }) {}

  close() {
    this.dialogRef.close();
  }

  get message(): string {
    return this.data.message;
  }
}
