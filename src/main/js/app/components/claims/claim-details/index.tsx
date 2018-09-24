import * as React from "react";
import { Query } from "react-apollo";
import gql from "graphql-tag";

import Grid from '@material-ui/core/Grid'

const CLAIM_PAGE_QUERY = gql`
  query ClaimPage($id: ID!) {
    getClaim(id: $id) {
      member {
        firstName
        lastName
        directDebitStatus {
          activated
        }
      }
      registrationDate
      recordingUrl
      type
      notes {
        text
        url
      }
      reserves
      payments {
        amount
        note
        type
        transaction {
          status
        }
      }
      events {
        text
        date
      }
    }
  }
`

interface Props {
  match: {
    params: {
      id: string
    }
  }
}

const ClaimPage: React.SFC<Props> = ({ match }) => (
  <Query query={CLAIM_PAGE_QUERY} variables={{ id: match.params.id }}>
    {({ loading, error, data }) => {
      if (loading) {
        return <div>Loading</div>
      }

      if (error) {
        return <div>Error: <pre>{JSON.stringify(error, null, 2)}</pre></div>
      }

      return (
        <Grid container>
          <Grid item>
            <div>Claim id: {match.params.id}<pre>{JSON.stringify(data, null, 2)}</pre></div>
          </Grid>
        </Grid>
      );
    }}
  </Query>
)

export default ClaimPage

// export default class ClaimDetails extends React.Component {
//   constructor(props) {
//     super(props);
//   }

//   componentDidMount() {
//     const { match, memberRequest, claimRequest, claimTypes } = this.props;
//     const id = match.params.id;
//     const userId = match.params.userId;
//     claimRequest(id);
//     memberRequest(userId);
//     claimTypes();
//   }

//   render() {
//     const {
//       claimDetails: { data },
//       createNote,
//       updateReserve,
//       createPayment,
//       match
//     } = this.props;
//     return (
//       <ClaimDetailsContainer>
//         <Header size="huge">Claim Details</Header>

//         {data ? (
//           <React.Fragment>
//             <ClaimInfo {...this.props} />
//             <Notes
//               notes={data.notes}
//               createNote={createNote}
//               id={match.params.id}
//             />
//             <Payments
//               claimDetails={data}
//               updateReserve={updateReserve}
//               createPayment={createPayment}
//               id={match.params.id}
//             />
//             <EventsLog events={data.events} />
//           </React.Fragment>
//         ) : null}
//       </ClaimDetailsContainer>
//     );
//   }
// }

// ClaimDetails.propTypes = {
//   claimDetails: PropTypes.object.isRequired,
//   createNote: PropTypes.func.isRequired,
//   updateReserve: PropTypes.func.isRequired,
//   match: PropTypes.object.isRequired,
//   claimRequest: PropTypes.func.isRequired,
//   claimTypes: PropTypes.func.isRequired,
//   createPayment: PropTypes.func.isRequired,
//   memberRequest: PropTypes.func.isRequired
// };
