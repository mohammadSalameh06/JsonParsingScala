import io.circe.generic.auto._
import io.circe.parser._



object JsonParser extends App {
  val jsonString = """
  {
    "stages": [
      {
        "name": "stage",
        "isParallel": true,
        "jobs": [
          {
            "name": "job1",
            "arguments": [
              "-i", "./input",
              "-o", "./output"
            ]
          }
        ]
      },
      {
        "name": "stage 2",
        "isParallel": true,
        "jobs": [
          {
            "name": "job1",
            "arguments": [
              "-i", "./input",
              "-o", "./output"
            ]
          },
          {
            "name": "job2",
            "arguments": [
              "-i", "./input",
              "-o", "./output"
            ]
          }
        ]
      }
    ]
  }
  """

  case class Job(
                  name: String,
                  arguments: List[String]
                )

  case class Stage(
                    name: String,
                    isParallel: Boolean,
                    jobs: List[Job]
                  )

  case class Pipeline(
                       stages: List[Stage]
                     )



implicit val jobDecoder :Decoder[Jobs] = addressCursor =>
  for
  {
    
  name <- addressCursor.get[String]("name")
  arguments <- addressCursor.get[List[String]]("arguments")
    
  } yield  Jobs(name,arguments)

  implicit val StageDecoder: Decoder[Stage] = addressCursor =>
    
    for {
      
      name <- addressCursor.get[String]("name")
      isParallel <- addressCursor.get[Boolean](" isParallel")
      jobs <- addressCursor.get[Jobs]("arguments")

    } yield Stage(name, isParallel, jobs)
    
    

  val decodedPipeline = decode[Pipeline](jsonString)

  decodedPipeline match {
    case Right(pipeline) =>
      println(s"Parsed pipeline: $pipeline")
    case Left(error) =>
      println(s"Failed to parse JSON: $error")
  }
}