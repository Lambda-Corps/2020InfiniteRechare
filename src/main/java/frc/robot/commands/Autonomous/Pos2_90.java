/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AlignWithVision;
import frc.robot.commands.DriveMM;
import frc.robot.commands.TurnMM;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import jdk.nashorn.api.tree.ParenthesizedTree;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Pos2_90 extends SequentialCommandGroup {
  /**
   * Creates a new Auto2.
   */
  public Pos2_90(DriveTrain driveTrain, Vision vision, Shooter shooter, Intake intake) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    addCommands(
      //new AlignWithVision(driveTrain, vision),
      new PrintCommand("shoot 3 balls"),
      new TurnMM(driveTrain, 90),
      new DriveMM(driveTrain, 58),
      new TurnMM(driveTrain, -90),
      parallel(
        new DriveMM(driveTrain,-158.13),
        new PrintCommand("drop intake")
      ),
      parallel(
        new DriveMM(driveTrain,158.13),
        new PrintCommand("raise intake")
      ),
      new TurnMM(driveTrain, -90),
      new DriveMM(driveTrain, 58),
      new TurnMM(driveTrain, 90),
      //new AlignWithVision(driveTrain, vision),
      new PrintCommand("shoot 3 balls")
    );
  }
}