//Utilizes enums and switch cases Finite State Machine to allow the arm slider to move to set heights
//automatically while maintaining control over the robot
//State Diagram attached later

switch (liftState)
{
   case LIFT_START:
       if(gamepad2.a)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_ONE);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_EXTEND_ONE;
       }

       if(gamepad2.b)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_TWO);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_EXTEND_TWO;
       }

       if(gamepad2.y)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_THREE);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_EXTEND_THREE;
       }
       break;
   case LIFT_EXTEND_ONE:
       if(Math.abs(armMotor.getCurrentPosition() - LIFT_LEVEL_ONE) < 10)
       {
           armMotor.setPower(0);
           liftState = LiftState.LIFT_DUMP;
       }
       break;
   case LIFT_EXTEND_TWO:
       if(Math.abs(armMotor.getCurrentPosition() - LIFT_LEVEL_TWO) < 10)
       {
           armMotor.setPower(0);
           liftState = LiftState.LIFT_DUMP;
       }
       break;
   case LIFT_EXTEND_THREE:
       if(Math.abs(armMotor.getCurrentPosition() - LIFT_LEVEL_THREE) < 10)
       {
           armMotor.setPower(0);
           liftState = LiftState.LIFT_DUMP;
       }
       break;
   case LIFT_DUMP:
       if(gamepad1.y)
       {
           Bucket.setPosition(DUMP_DEPOSIT);
           dumpTimer.reset();
           liftState = LiftState.LIFT_DUMP_TIMER;
       }

       if(gamepad2.a)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_ONE);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_EXTEND_ONE;
       }

       if(gamepad2.b)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_TWO);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_EXTEND_TWO;
       }

       if(gamepad2.y)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_THREE);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_EXTEND_THREE;
       }
       break;
   case LIFT_DUMP_TIMER:
       if(dumpTimer.seconds() >= DUMP_TIME)
       {
           liftState = LiftState.LIFT_DUMP_RETRACT;
       }
       break;
   case LIFT_DUMP_RETRACT:
       Bucket.setPosition(DUMP_IDLE);
       dumpResetTimer.reset();
       liftState = LiftState.LIFT_DUMP_RETRACT_TIMER;
       break;
   case LIFT_DUMP_RETRACT_TIMER:
       if(dumpResetTimer.seconds() >= DUMP_RETRACT_TIME)
       {
           armMotor.setTargetPosition(LIFT_LEVEL_ZERO);
           armMotor.setPower(ARM_SPEED);
           armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
           liftState = LiftState.LIFT_RETRACT;
       }
       break;
   case LIFT_RETRACT:
       if(Math.abs(armMotor.getCurrentPosition() - LIFT_LEVEL_ZERO) < 10)
       {
           armMotor.setPower(0);
           liftState = LiftState.LIFT_START;
       }
       break;
   default:
       liftState = LiftState.LIFT_START;
}

//safety button to reset the states
if (gamepad2.x && liftState != LiftState.LIFT_START)
{
   Bucket.setPosition(DUMP_IDLE);
   dumpResetTimer.reset();
   liftState = LiftState.LIFT_DUMP_RETRACT_TIMER;
}
