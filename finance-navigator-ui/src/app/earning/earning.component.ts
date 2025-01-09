import { Component, Inject, Input } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AddEarningDialogComponent } from './add-earning-dialog/add-earning-dialog.component';
import { ApiService } from '../api.service';
import { EditEarningDialogComponent } from './edit-earning-dialog/edit-earning-dialog.component';
@Component({
  selector: 'app-earning',
  templateUrl: './earning.component.html',
  styleUrls: ['./earning.component.css']
})
export class EarningComponent {
  @Input() data: any;

  constructor(public dialog: MatDialog, public apiService: ApiService,
){}

  openAddEarningDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = this.data;

    const dialogRef = this.dialog.open(AddEarningDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((result) => {
      
    });
  }

  openEditEarningDialog(earningId: number, earningAmount:any, earningDate:any) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = this.data;
    dialogConfig.data.earningId = earningId;
    dialogConfig.data.earningAmount = earningAmount;
    dialogConfig.data.earningDate = earningDate;

    const dialogRef = this.dialog.  open(EditEarningDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe((result) => {

    });
  }

  deleteEarning(earningId: number){
    console.log("Deleting "+earningId);
    this.apiService.deleteEarning(earningId).subscribe(
      ()=>{
        window.location.reload();
      }
    );
    
  }
}
