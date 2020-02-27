/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonCommands.autonCommandGroups;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.commands.ConveyorCommand;
import frc.robot.commands.IndexToBlasterCommand;
import frc.robot.commands.ManualTurretCommand;
import frc.robot.commands.TurnToTargetCommand;
import frc.robot.commands.autonCommands.DriveForwardGyroDistanceCommand;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.TurretSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Yeet3 extends SequentialCommandGroup {
  /**
   * Creates a new Yeet3.
   */
  public Yeet3(TurretSubsystem turretSubsystem, DriveTrainSubsystem driveTrainSubsystem, ConveyorSubsystem conveyorSubsystem, IntakeSubsystem intakeSubsystem) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
      new ManualTurretCommand(turretSubsystem, ()->0, ()->1),
      new TurnToTargetCommand(turretSubsystem),
      new ParallelDeadlineGroup(
        new WaitCommand(5), 
        new IndexToBlasterCommand(intakeSubsystem),
        new ConveyorCommand(conveyorSubsystem, ()->1)
      ),
      new DriveForwardGyroDistanceCommand(driveTrainSubsystem, Constants.AUTON_DRIVE_OFF_LINE_DISTANCE, Constants.AUTON_DRIVE_OFF_LINE_SPEED, 0, true)
    );
  }
}