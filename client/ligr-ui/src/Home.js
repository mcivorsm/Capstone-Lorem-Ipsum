
function Home() {
  return (
    <div>
      <h2>HomePage for Lorem Ipsum Game Reviews</h2>
    <section>
      <h3 className="mb-4">Top 10 Games</h3>
      <table className="table table-striped table-hover">
        <thead className="thead-dark">
          <tr>
            <th>Name</th>
            <th>Developer - Producer</th>
            <th>Year Released</th>
            <th>Top Sale Region</th>
            <th>Rating</th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          {/* populate table with map function */}
        </tbody>
      </table>
    </section>
    </div>
  );
}

export default Home;